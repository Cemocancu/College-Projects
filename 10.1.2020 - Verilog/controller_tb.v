`timescale 1ms / 1ns

module controller_tb;

    reg clk, cngstn;
    wire [1:0] main_stt, side_stt;
    
    controller uut (.clock(clk), .congestion(cngstn), .main_road(main_stt), .side_road(side_stt));
    
    initial begin
        cngstn <= 1;
    end
    
    initial begin
        #500;
        clk = 0;
        forever begin
            #500;
            clk = ~clk;
        end
    end
    
    always@(posedge clk)begin
        #20000
        cngstn = ~cngstn;
    end

endmodule
